package pl.writeonly.scalaops.future.api

import pl.writeonly.scalaops.ops.FutureOps
import pl.writeonly.scalaops.ops.mono.api.ToThrowable
import pl.writeonly.scalaops.pipe.Pipe

trait Utils extends Pipe with ToThrowable with FutureOps
