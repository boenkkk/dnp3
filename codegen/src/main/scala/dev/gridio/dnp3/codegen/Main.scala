package dev.gridio.dnp3.codegen

import java.nio.file.{FileSystems, Path}

import dev.gridio.dnp3.codegen.render._
import dev.gridio.dnp3.codegen.render.modules._

object Main {

  val appPath: Path = FileSystems.getDefault.getPath("../src/app/")
  val implPath: Path = appPath.resolve("gen");

  object CommonUseStatements extends Module {
    override def lines(implicit indentation: Indentation): Iterator[String] = {
      "use crate::app::parse::traits::{FixedSize, FixedSizeVariation};".eol ++
      "use crate::util::cursor::*;".eol ++
      "use crate::app::enums::CommandStatus;".eol ++
      "use crate::app::types::{ControlCode, Timestamp};".eol ++
      "use crate::app::flags::format::*;".eol
    }
  }

  def modules : List[(Module, Path)] = List(
    // these have some publicly exported stuff
    (ProtocolEnums,  appPath.resolve("enums.rs")),
    (CommonUseStatements ++ VariationEnumModule ++ FixedSizeVariationModule,  appPath.resolve("variations.rs")),
    // these don't contain any publicly exported stuff
    (RangedVariationModule,  implPath.resolve("ranged.rs")),
    (AllObjectsVariationModule,  implPath.resolve("all.rs")),
    (CountVariationModule, implPath.resolve("count.rs")),
    (PrefixedVariationModule, implPath.resolve("prefixed.rs")),
    (ConversionsModule, implPath.resolve("conversion.rs"))
  )

  def main(args: Array[String]): Unit = {
    modules.foreach { case (m,p) => writeTo(p)(m) }
  }

}
