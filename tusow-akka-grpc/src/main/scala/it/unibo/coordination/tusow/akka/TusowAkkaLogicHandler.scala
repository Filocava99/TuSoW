package it.unibo.coordination.tusow.akka

import akka.NotUsed
import akka.stream.scaladsl.Source
import it.unibo.coordination.linda.core.TupleSpace
import it.unibo.coordination.linda.logic.{LogicMatch, LogicSpace, LogicTemplate, LogicTuple}
import it.unibo.coordination.tusow.grpc.{IOResponse, IOResponseList, ReadOrTakeAllRequest, ReadOrTakeRequest, Template, Tuple, TupleSpaceID, TuplesList, TusowService, WriteAllRequest, WriteRequest}
import it.unibo.tuprolog.core.Term

import java.util.concurrent.CompletableFuture
import scala.jdk.FutureConverters._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class TusowAkkaLogicHandler extends TusowService {

    type LogicSpace = TupleSpace[LogicTuple, LogicTemplate, String, Term, LogicMatch]
    val logicSpaces = new scala.collection.mutable.HashMap[String, LogicSpace]

    override def validateTupleSpace(in: TupleSpaceID): Future[IOResponse] = Future.successful(IOResponse(logicSpaces.contains(in.id)))

    override def createTupleSpace(in: TupleSpaceID): Future[IOResponse] = {
        logicSpaces(in.id) = LogicSpace.local(in.id)
        Future.successful(IOResponse(response = true))
    }

    override def write(in: WriteRequest): Future[IOResponse] = {
        val space = logicSpaces(in.tupleSpaceID.getOrElse(TupleSpaceID("")).id)
        if(space == null){
            Future.successful(IOResponse(response = false, message = "Tuple space not found"))
        }else{
            space.write(in.tuple.get.value).asScala.map(f => IOResponse(response = true, message = f.toString))
        }
    }

    override def read(in: ReadOrTakeRequest): Future[Tuple] = {
        val space = logicSpaces(in.tupleSpaceID.getOrElse(TupleSpaceID("")).id)
        if (space == null) {
            Future.successful(null)
        } else {
            space.read(in.template.logicTemplate.getOrElse(Template.Logic()).query).asScala.map(logicMatch => Tuple(key = logicMatch.getTemplate.toString, value = logicMatch.getTuple.orElse(LogicTuple.of("")).getValue.toString))
        }
    }

    override def take(in: ReadOrTakeRequest): Future[Tuple] = {
        val space = logicSpaces(in.tupleSpaceID.getOrElse(TupleSpaceID("")).id)
        if (space == null) {
            Future.successful(null)
        } else {
            space.take(in.template.logicTemplate.getOrElse(Template.Logic()).query).asScala.map(logicMatch => Tuple(key = logicMatch.getTemplate.toString, value = logicMatch.getTuple.orElse(LogicTuple.of("")).getValue.toString))
        }
    }

    override def writeAll(in: WriteAllRequest): Future[IOResponseList] = {
        val space = logicSpaces(in.tupleSpaceID.getOrElse(TupleSpaceID("")).id)
        if (space == null) {
            Future.successful(null)
        }else{
            val futures = in.tuplesList.get.tuples.map(t => space.write(t.value))
            var ioResponseList = scala.Seq[IOResponse]()
            val composedFuture = futures.foldLeft(CompletableFuture.allOf(futures.head))((f1, f2) => CompletableFuture.allOf(f1, f2))
            composedFuture.thenApplyAsync(_ => {
                futures.foreach(f => {
                    ioResponseList = ioResponseList :+ IOResponse(response = true, message = f.get.toString)
                })
                IOResponseList(ioResponseList)
            }).asScala
        }
    }

    override def readAll(in: ReadOrTakeAllRequest): Future[TuplesList] = ???

    override def takeAll(in: ReadOrTakeAllRequest): Future[TuplesList] = ???

    override def writeAllAsStream(in: WriteAllRequest): Source[IOResponse, NotUsed] = ???

    override def readAllAsStream(in: ReadOrTakeAllRequest): Source[Tuple, NotUsed] = ???

    override def takeAllAsStream(in: ReadOrTakeAllRequest): Source[Tuple, NotUsed] = ???
}
