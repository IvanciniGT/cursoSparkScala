import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.{DataFrame, Row, SparkSession}


object PersonasSQL {

    def main(args: Array[String]): Unit = {

      // Conectar con un cluster de Spark
      val conexion: SparkSession = SparkSession.builder()
                                               .appName("Procesamiento de personas")
                                               .master("local[5]") // Para ejecutar en local con todos los núcleos disponibles
                                               .getOrCreate()
      // personas.json
      // {"nombre":"Juan","apellidos":"García","edad":25, "dni": "23000000T", "cp": "28001", "email": "juan@garcia.es"}
      val personas = conexion.read.json("src/main/resources/personas.json") // en la realidad esto debe estar en un recurso en RED...
      // personas.show()// Dado que el archivo puede ser grande... lo que haría es limitar el número de filas que se muestran, por ejemplo: personas.show(10) para mostrar solo las 10 primeras filas.
      personas.show(10)
      personas.printSchema()
      // Y usar una url del tipo: nfs://direccion/personas.json, o hdfs://direccion/personas.json, o s3a://direccion/personas.json, etc... dependiendo del sistema de ficheros distribuido que estemos usando.

      // cps.csv  <- Hay un header con el nombre de las columnas, y luego cada fila tiene el código postal y el municipio al que corresponde.
      // cp,municipio,provincia
      // "28000","Madrid","Madrid"
      val codigosPostales = conexion.read.option("header", "true").csv("src/main/resources/cps.csv") // en la realidad esto debe estar en un recurso en RED...
      codigosPostales.show(10)
      codigosPostales.printSchema()

      // Validación de los DNIs
      // Ayer en el Archivo Intro hicimos esto con una UDF... y trabajando con SQL.
      // Eso es lo mejor en este caso! Pero eso ya lo vimos ayer...
      // por lo que hoy vamos a hacer algo diferente... solo como ejercicio y para aprender algo nuevo

      // Comentamos ayer que podemos pasar de un DataFrame a un RDD y viceversa.
      // Vamos a aplicar el filtro en un RDD... de hecho vamos a generar una columna nueva en el Dataframe (desde el RDD) con la normalización del DNI y su validez.
      val personasComoRDD: RDD[Row] = personas.rdd  // Las filas (ROW) tienen dentro columnas
      // Ahora puedo aplicar programación MapReduce pura.
      // Row (4 datos) -> map -> Row (6 datos)
      val personasComoRDDConInformacionDeDNI =  personasComoRDD.map (
        (filaCon4Columnas:Row) => {
          // Tenemos que producir una Row con 6 columnas: nombre, apellidos, edad, dni, dni_normalizado, dni_valido
          // De hecho, lo que podríamos es agregar las 2 columnas nuevas a la fila:
          val dni = filaCon4Columnas.getAs[String]("dni") // Obtenemos el valor de la columna dni de la fila
          // Validamos el dni con nuestra libreria y le calculamos el formato:
          val potencialDNI:Either[DniError, DNI] = DNI.parse(dni)
          var dniValido: Boolean = false
          var dniNormalizado: String = "DNI no válido"
          if(potencialDNI.isRight) {
          //potencialDNI.toOption.foreach( dniParseado => {
            dniValido = true
            dniNormalizado =potencialDNI.toOption.get.formatear()
          }
        //)
          // Ya tenemos el formato del DNI, y su validez... ahora lo que tenemos que hacer es generar una nueva fila con toda la información:
          Row.fromSeq(filaCon4Columnas.toSeq ++ Seq(dniNormalizado, dniValido)) // Concatenamos la fila original con las 2 nuevas columnas
        }
      ) // Esto es una locura comparado con el haber usado un udf como hicimos ayer...
      // Solo queremos aprender a pasar de un dataframe a un RDD, y viceversa
      // Y nos falta el viceversa
      // Cuando creamos un dataframe, necesitamos siempre un esquema de datos.
      // Al leer de un csv o de un json, el esquema se genera automáticamente a partir de los datos.
      // Pero cuando creamos un dataframe a partir de un RDD, tenemos que definir el esquema nosotros mismos.
      // No necesitamos hacerlo de cero... al fin y al cabo, solo estamos extendiendo el esquema original con 2 columnas nuevas... por lo que podemos partir del esquema original, y añadirle las 2 columnas nuevas.
      val esquemaOriginal = personas.schema
      val esquemaNuevo = esquemaOriginal.add("dni_normalizado", "string").add("dni_valido", "boolean") // Añadimos las 2 columnas nuevas al esquema original
      val personasConInformacionDeDNI: DataFrame = conexion.createDataFrame(personasComoRDDConInformacionDeDNI, esquemaNuevo) // Creamos un nuevo dataframe a partir del RDD con el nuevo esquema
      personasConInformacionDeDNI.show(10)
      personasConInformacionDeDNI.printSchema()

      personas.createOrReplaceTempView("personas") // Registramos el dataframe como una tabla temporal llamada "personas")
      conexion.udf.register(
        "validarDNI", // nombre SQL para la función"
        udf(
          (dni: String) => DNI.esValido(dni)
        )
      )
      conexion.udf.register(
        "formatearDNI", // nombre SQL para la función"
        udf(
          (dni: String) => DNI.parse(dni) match {
            case Left(error) => s"DNI no válido: $error"
            case Right(dni) => dni.formatear()
          }
        )
      )
      val personasConInformacionDeDNI2 = conexion.sql("""
          SELECT
                *,
                validarDNI(dni) as dni_valido,
                formatearDNI(dni) as dni_formateado,
                edad >= 18 as mayor_edad
          FROM
            personas
        """).cache() // Esto hará que esas transformaciones solo se ejecuten 1 vez, y el resultado se guarde en memoria para las siguientes consultas.

      personasConInformacionDeDNI2.show(10)
      personasConInformacionDeDNI2.filter("dni_valido = false").write.mode("overwrite").parquet("personas_con_dni_invalido.parquet") // Solo las personas con DNI válido
      val personasDNIOK = personasConInformacionDeDNI2.filter("dni_valido = true").cache()
      personasDNIOK.show(10)
      personasDNIOK.filter("mayor_edad = true").write.mode("overwrite").parquet("personas_guay.parquet") // Solo las personas con DNI no válido
      personasDNIOK.filter("mayor_edad = false").write.mode("overwrite").parquet("personas_menores_con_dni_valido.parquet") // Solo las personas con DNI no válido y menores de edad

      // Cuál es la función de reducción que estoy aplicando? .show()
      // Cuántas veces aplico esa función? 3
      // Cuántas veces se está ejecutando: validarDNI? y formatearDNI? 3 o 1?
      // personasConInformacionDeDNI2 es una tabla que contiene los campos dni_valido y dni_formateado? NO solo tiene anotado que deben calcularse
      // Respuesta: 3 veces... cada vez que hemos ejecutado el pipeline (map-reduce) de procesamiento de datos.
      // Para evitar esto, ponemos cache()

      // En Intellij, puedo poner un punto de debug en el stop... para que se detenga ahi... y el UI de spark se quede corriendo.
      // Otra opcion que tengo es hacer un sleep del hilo:
      Thread.sleep(1000000)
      conexion.stop()
    }

}
