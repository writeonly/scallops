package pl.writeonly.scalaops.api.future

import pl.writeonly.scalaops.api.present.ToThrowable
import pl.writeonly.scalaops.ops.FutureOps
import pl.writeonly.scalaops.pipe.Pipe

trait Utils extends Pipe with ToThrowable with FutureOps
