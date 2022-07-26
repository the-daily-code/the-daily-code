use std::env;

fn main() {
    print!("Content-type: text/plain\r\n\r\n");
    println!("Hello, world!");

    for (k, v) in env::vars_os() {
        println!("{:?}: {:?}", k, v);
    }
}
