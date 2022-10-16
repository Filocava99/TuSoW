package it.unibo.coordination.tusow.akka

import akka.NotUsed
import akka.stream.scaladsl.Source
import it.unibo.coordination.tusow.grpc.{IOResponse, IOResponseList, ReadOrTakeAllRequest, ReadOrTakeRequest, Tuple, TupleSpaceID, TuplesList, TusowService, WriteAllRequest, WriteRequest}

import scala.concurrent.Future

class TusowAkkaService extends TusowService{
    override def validateTupleSpace(in: TupleSpaceID): Future[IOResponse] = {
        Future.successful(IOResponse(response = true))
    }

    override def createTupleSpace(in: TupleSpaceID): Future[IOResponse] = ???

    override def write(in: WriteRequest): Future[IOResponse] = ???

    override def read(in: ReadOrTakeRequest): Future[Tuple] = ???

    override def take(in: ReadOrTakeRequest): Future[Tuple] = ???

    override def writeAll(in: WriteAllRequest): Future[IOResponseList] = ???

    override def readAll(in: ReadOrTakeAllRequest): Future[TuplesList] = ???

    override def takeAll(in: ReadOrTakeAllRequest): Future[TuplesList] = ???

    override def writeAllAsStream(in: WriteAllRequest): Source[IOResponse, NotUsed] = ???

    override def readAllAsStream(in: ReadOrTakeAllRequest): Source[Tuple, NotUsed] = ???

    override def takeAllAsStream(in: ReadOrTakeAllRequest): Source[Tuple, NotUsed] = ???
}
