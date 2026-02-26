import org.apache.spark.sql.SparkSession


case class Persona(nombre: String, edad: Int, municipio: String, dni: String)
object IntroSparkSQL {

    def main(args: Array[String]): Unit = {

        // Conectar con un cluster de Spark
        val conexion: SparkSession = SparkSession.builder()
                                                 .appName("Mi primera aplicación con Spark SQL")
                                                 .master("local[5]") // Para ejecutar en local con todos los núcleos disponibles
                                                 .getOrCreate()
        // Procesamos datos
        val datos = List(
            Persona("Ivan", 30, "Madrid", "230.000-t"),
            Persona("Menchu", 28, "Madrid", "87654321B"),
            Persona("Pepe", 35, "Barcelona", "11223344C"),
            Persona("Ana", 22, "Valencia", "44332211D")
        )
        val personas = conexion.createDataFrame(datos)
        personas.show()
        personas.printSchema()

        // Como las tablas de las BBDD, los dataframes tiene un esquema.
        // Ese esquema se genera automáticamente a partir de la clase Persona.
        // Cuando llevamos datos de un Parquet, CSV, AVRO, etc... el esquema se generará a partir de los datos que haya en ese fichero.
        // En general SparkSQL hace un buen trabajo al definir el esquema a partir de los datos, pero a veces es necesario definirlo nosotros mismos (por ejemplo, si el fichero no tiene cabecera, o si queremos forzar un tipo de dato concreto para una columna)
        // Podemos acceder directamente al esquema y modificarlo, o podemos definir el esquema nosotros mismos y decirle a Spark que lo use en lugar del que él ha inferido a partir de los datos.
        // personas.schema // Esquema inferido por Spark a partir de los datos. Es un objeto de tipo StructType, que es una lista de StructField (nombre, tipo, nullable)

        // OJO! Que a veces se nos va la olla!
        // Una cosa es que sparksql me permita usar una sintaxis similar a SQL para escribir consultas, y otra cosa es que realmente esté ejecutando esas consultas en un motor de base de datos relacional.
        // AQUI NO HAY BBDD. Y las BBDD son seres maravillosos... extraordinarios. COMPLEJOS!

        // SINTAXIS PROGRAMATICA: Hacemos uso de conceptos SQL , pero con la sintaxis de Scala.
        personas.select("nombre","edad").show()
        personas.filter("edad > 30").show()
        personas.groupBy("municipio").count().show()
        personas.groupBy("municipio").avg("edad").show()

        import org.apache.spark.sql.functions._
        personas.select(col("nombre"), col("edad")).show() // Seleccionamos todas las columnas
        // LA gracia del col es que me genera un objeto de tipo Column, que y eso objeto tiene un montón de métodos para hacer operaciones con esa columna... también admite operaciones... incluso hay otras funciones que aplican sobre objetos column.
        personas.select( upper(col("nombre")).as("nombre_mayusculas"), (col("edad") + 17).as("edad_incrementada")     ).show() // Seleccionamos todas las columnas, pero la columna nombre la pasamos a mayúsculas

        // SINTAXIS SQL pura y dura.
        // Para ello, lo que tenemos que hacer es registrar el dataframe como una tabla temporal, y luego ya podemos escribir consultas SQL sobre esa tabla.
        personas.createOrReplaceTempView("personas") // Registramos el dataframe como una tabla temporal llamada "personas")
        // A partir de este momento puedo usarla en queries SQL:
        conexion.sql("""
          SELECT
                upper(nombre) as nombre_mayusculas,
                edad + 17 as edad_incrementada
          FROM
            personas
          WHERE
            municipio = 'Madrid'
        """).show();
        // Esto tiene una gracia adicional... Recordamos los broadcast?
        // Cuando registramos una tabla como temporal, Spark la guarda en memoria RAM de cada nodo del cluster... y la hace accesible para todas las consultas SQL que se hagan sobre esa tabla.
        // Es decir, se hace un broadcast de esa tabla .

        // Además de las funciones de ansiSQL, que se incluyen en SparkSQL, tenemos la posibilidad de registrar
        // dentro de SparkSQL nuestras propias funciones SCALA. Esto es lo que llamamos UDF (User Defined Functions).
        // Es decir, funciones definidas por el usuario.
        conexion.udf.register(
            "validarDNI", // nombre SQL para la función"
            udf(
              (dni: String) => DNI.esValido(dni)
            )
        )
        conexion.sql("""
          SELECT
                upper(nombre) as nombre_mayusculas,
                edad + 17 as edad_incrementada,
                validarDNI(dni) as dni_valido
          FROM
            personas
          WHERE
            municipio = 'Madrid'
        """).show()

      conexion.udf.register(
        "formatearDNI", // nombre SQL para la función"
        udf(
          (dni: String) => DNI.parse(dni) match {
            case Left(error) => s"DNI no válido: $error"
            case Right(dni) => dni.formatear()
          }
        )
      )
      conexion.sql("""
          SELECT
                upper(nombre) as nombre_mayusculas,
                edad + 17 as edad_incrementada,
                validarDNI(dni) as dni_valido,
                dni,
                formatearDNI(dni) as dni_formateado
          FROM
            personas
          WHERE
            municipio = 'Madrid'
        """).show()

      conexion.sql("""
          SELECT
                upper(nombre) as nombre_mayusculas,
                edad + 17 as edad_incrementada,
                validarDNI(dni) as dni_valido,
                dni,
                formatearDNI(dni) as dni_formateado
          FROM
            personas
          WHERE
            municipio = 'Madrid'
        """).show() //.write.mode("overwrite").parquet("./salida.parquet") // Escribimos el resultado de la consulta en un fichero parquet. Si el fichero ya existe, lo sobrescribimos.
      // Dónde se guardaría ese archivo?
      // Se guarda en la carpeta desde donde se esté ejecutando el programa (JVM) en el nodo del clsuter trabajador que esté haciendo esto.
      // De hecho... este trabajo por dejbajo, cuántos nodos los están ejecutando? Varios..
      // Cada uno guardaría sus datos en una carpeta local!
      // Ahí usariamos rutas en red, normalmente hdfs://...
/*
      conexion.read.json("./personas.json").show() // Leemos un fichero JSON y lo mostramos por pantalla. Spark infiere el esquema a partir de los datos del JSON.
      conexion.read
                  .option("header", "true") // Le decimos a Spark que el CSV tiene una cabecera con los nombres de las columnas
                  .csv("./personas.csv").show() // Leemos un fichero CSV y lo mostramos por pantalla. Spark infiere el esquema a partir de los datos del CSV.
*/
        // Cierro conexión al cluster
        conexion.stop()
    }

}
