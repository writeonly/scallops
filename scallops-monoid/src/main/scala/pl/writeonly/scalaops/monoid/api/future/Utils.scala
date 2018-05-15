package pl.writeonly.scalaops.monoid.api.future

import pl.writeonly.scalaops.monoid.api.present.ToThrowable
import pl.writeonly.scalaops.ops.FutureOps
import pl.writeonly.scalaops.pipe.Pipe

trait Utils extends Pipe with ToThrowable with FutureOps
