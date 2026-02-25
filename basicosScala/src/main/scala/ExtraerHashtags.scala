object ExtraerHashtags {

  def main(args: Array[String]): Unit = {

    val tweets = List(
      "En la playa con mis amig@s #veranito#GoodVibes#FriendForever#PedoPis",
      "En casa con mi perrito #DogLover#PetLover#GoodVibes#CuloCaca",
      "Preparando la oposición #MierdaVibes#Estudio#QuieroSerFuncionaria",
      "1, 2,3 nos vamos otra vez! #MasVerano#GoodVibes #PartyAllNight#OtroCaca"
    )

    val palabrasProhibidas = List("caca", "culo", "pedo", "pis", "mierda")

    var numeroDeHashtagsEliminados = 0L

    val resultado: Map[String, Int] = // TODO

    println(s"Número de hashtags eliminados: $numeroDeHashtagsEliminados")

    for (par <- resultado) {
    val hashtag = par._1
    val ocurrencias = par._2
    println(hashtag + " -> " + ocurrencias)
    }  
  }
}