mod utils;

use wasm_bindgen::prelude::*;

// When the `wee_alloc` feature is enabled, use `wee_alloc` as the global
// allocator.
#[cfg(feature = "wee_alloc")]
#[global_allocator]
static ALLOC: wee_alloc::WeeAlloc = wee_alloc::WeeAlloc::INIT;

static mut GRAPHICS: [u8; 64 * 32] = [0; 64 * 32];

#[wasm_bindgen]
extern "C" {
    #[wasm_bindgen(js_namespace = console)]
    fn log(s: &str);
}

#[wasm_bindgen]
pub fn game_start() {
    log("game is starting");
    // TODO:
}

#[wasm_bindgen]
pub fn game_tick(elapsed: f64) {
    // TODO:
}

#[wasm_bindgen]
pub fn game_key_pressed(key_code: u32, down: bool) {
    // TODO:
}

#[wasm_bindgen]
pub fn game_graphics_pointer() -> *const u8 {
    let pointer: *const u8;
    unsafe {
        pointer = GRAPHICS.as_ptr();
    }
    pointer
}
