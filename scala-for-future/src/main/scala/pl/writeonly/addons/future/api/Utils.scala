package pl.writeonly.addons.future.api

import pl.writeonly.addons.ops.{FutureOps, ToThrowable}
import pl.writeonly.addons.pipe.Pipe

trait Utils extends Pipe with ToThrowable with FutureOps
