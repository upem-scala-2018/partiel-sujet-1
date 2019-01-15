package fr.upem.partiel.part2.functions

import fr.upem.partiel.part2.model.Movie
import fr.upem.partiel.part2.model.Movie.Director

object Functions {

  // TODO
  lazy val getDirectorNames: List[Movie] => List[String] = movies => movies.map(m => s"${m.director.firstName} ${m.director.lastName}")

  // TODO
  lazy val viewMoreThan: Long => List[Movie] => List[Movie] = v => movies => movies.filter(m => m.views.value > v)

  // TODO
  lazy val byDirector: List[Movie] => Map[Director, List[Movie]] = movies => movies.groupBy(m => m.director)

}
