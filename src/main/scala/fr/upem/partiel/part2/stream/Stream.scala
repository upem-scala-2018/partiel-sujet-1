package fr.upem.partiel.part2.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.{Done, NotUsed}
import fr.upem.partiel.part2.model.Movie
import fr.upem.partiel.part2.model.Movie.{Country, Director}

import scala.concurrent.Future

object Stream {

  implicit lazy val system = ActorSystem("system")
  implicit lazy val materializer = ActorMaterializer()

  type SimpleSource[A] = Source[A, NotUsed]
  type SimpleFlow[A, B] = Flow[A, B, NotUsed]
  type SimpleSink[A] = Sink[A, Future[Done]]

  // TODO Create a source of Movies
  def sourceOf[A](movies: List[A]): SimpleSource[A] = Source.apply(movies)

  // TODO Create a flow to serialize movies, using a Show typeclass instance
  trait Show[A] {
    def show(a: A): String
  }

  implicit val CountryShow = new Show[Country] {
    override def show(c: Country) = c match {
      case Country.UnitedStates => "an american"
      case Country.France => "a french"
      case Country.England => "an english"
      case Country.Italy => "an italian"
      case Country.Germany => "a german"
    }
  }

  implicit val DirectorShow = new Show[Director] {
    override def show(d: Director) = s"${d.firstName} ${d.lastName}"
  }

  implicit def MovieShow(implicit countryShow: Show[Country], directorShow: Show[Director]) = new Show[Movie] {
    override def show(m: Movie) = s"${m.title.value} is ${countryShow.show(m.country)} movie directed by ${directorShow.show(m.director)} in ${m.year.value}"
  }

  def serialize[A: Show]: SimpleFlow[A, String] = Flow.fromFunction(implicitly[Show[A]].show)

  // TODO Create a flow to transform movies, using a Functor typeclass instance
  trait Functor[F[_]] {
    def map[A, B](f: A => B)(fa: F[A]): F[B]
  }

  implicit val OptionFunctor = new Functor[Option] {
    override def map[A, B](f: A => B)(fa: Option[A]) = fa match {
      case Some(x) => Some(f(x))
      case None => None
    }
  }

  def transform[F[_] : Functor, A, B](f: A => B): SimpleFlow[F[A], F[B]] = Flow.fromFunction(implicitly[Functor[F]].map(f))

  // TODO Implements the simple transformation function
  val movieToDirector: Movie => Director = _.director

  // TODO Create a flow to filter movies, with an Eq typeclass instance
  trait Eq[A] {
    def eq(a: A, b: A): Boolean
  }

  implicit val MovieEq = new Eq[Movie] {
    override def eq(a: Movie, b: Movie) = a == b
  }

  def getOnly[A: Eq](a: A): SimpleFlow[A, Option[A]] = Flow.fromFunction(x => Some(x).filter(implicitly[Eq[A]].eq(_, a)))

  def sink[A]: SimpleSink[A] = Sink.foreach(println)

}
