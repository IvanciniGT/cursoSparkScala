import org.apache.spark.{SparkConf, SparkContext}

object FiltrarDNIsConSparkCore {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
      .setAppName("Filtrar DNIs con Spark Core")
      .setMaster("local[*]")

    val sc = new SparkContext(conf)

    try {
      // Generamos 1M
      val total = 1_000_000
      val porcentajeValidos = 0.7
      val seed = 42L

      val dnis: Array[String] = GeneradorDNIs.generarLote(total, porcentajeValidos, seed)

      val rdd = sc.parallelize(dnis)

      // Acumulador para contar inválidos
      val invalidosAcc = sc.longAccumulator("dnis_invalidos")

      // Parseo + filtro en un solo paso:
      // - Si parse es Right(dni) => lo emito
      // - Si parse es Left(err)  => incremento acumulador y no emito nada
      val dnisValidosRDD = rdd.flatMap { texto =>
        DNI.parse(texto) match {
          case Right(dni) =>
            Some(dni) // emitimos el DNI válido

          case Left(_: DniError) =>
            invalidosAcc.add(1)
            None // descartamos el inválido
        }
      }

      // Fuerza ejecución (si no hay action, el acumulador no se actualiza)
      val dnisValidos = dnisValidosRDD.collect()

      println(s"DNIs válidos: ${dnisValidos.length}")
      println(s"DNIs inválidos (acc): ${invalidosAcc.value}")

      // Mostrar algunos válidos
      dnisValidos.take(20).foreach(dni => println(dni.formatear()))

    } finally {
      sc.stop()
    }
  }
}