package pl.writeonly.scalaops.future.api

import pl.writeonly.addons.ops.ToThrowable
import pl.writeonly.scalaops.ops.{FutureOps, ToThrowable}
import pl.writeonly.scalaops.pipe.Pipe

trait Utils extends Pipe with ToThrowable with FutureOps
