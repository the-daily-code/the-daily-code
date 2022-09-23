(module
 (func $mul (param $lhs i64) (param $rhs i64) (result i64)
       local.get $lhs
       local.get $rhs
       i64.mul)
 (export "mul" (func $mul)))
