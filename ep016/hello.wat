(module
  (func $print (import "imports" "print_func") (param i64))
  (func $mul (param $lhs i64) (param $rhs i64) (result i64)
    local.get 0
    local.get 1
    ;; stack
    i64.mul)
  (func $print_mul (param $lhs i64) (param $rhs i64)
    local.get $lhs
    local.get $rhs
    call $mul
    call $print)
  (export "mul" (func $mul))
  (export "print_mul" (func $print_mul)))