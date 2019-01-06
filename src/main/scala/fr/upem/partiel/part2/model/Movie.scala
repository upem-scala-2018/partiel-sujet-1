package fr.upem.partiel.part2.model

// TODO You have to create all the classes you need for the exam
// TODO Don't forget to read the existing code and the unit tests to get some clues !

// TODO Create this model
trait Movie

object Movie {

  def apply: Movie = ???

  // TODO Create this model
  trait Title

  // TODO Create this model
  trait Director

  // TODO Create this model
  trait Year

  // TODO Create this model
  trait Views

  trait Country

  object Country {

    final case object France extends Country

    final case object England extends Country

    final case object Italy extends Country

    final case object Germany extends Country

    final case object UnitedStates extends Country

  }

  // TODO Create this method
  def movie(title: Title, director: Director, year: Year, views: Views, country: Country): Movie = ???

  // TODO Create this method
  def title(s: String): Title = ???

  // TODO Create this method
  def director(fn: String, ln: String): Director = ???

  // TODO Create this method
  def year(value: Int): Year = ???

  // TODO Create this method
  def views(value: Long): Views = ???

}