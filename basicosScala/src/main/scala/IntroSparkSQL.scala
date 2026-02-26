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
            Persona("Ivan", 30, "Madrid", "12345678A"),
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

        
        // Cierro conexión al cluster
        conexion.stop()
    }

}
