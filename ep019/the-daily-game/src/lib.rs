mod utils;

use wasm_bindgen::prelude::*;
use crate::utils::set_panic_hook;

// When the `wee_alloc` feature is enabled, use `wee_alloc` as the global
// allocator.
#[cfg(feature = "wee_alloc")]
#[global_allocator]
static ALLOC: wee_alloc::WeeAlloc = wee_alloc::WeeAlloc::INIT;

const WIDTH: usize = 64;
const HEIGHT: usize = 32;

type Graphics = [u8; WIDTH * HEIGHT];

struct Pixel { x: i32, y: i32 }

impl Pixel {
    const fn new(x: i32, y: i32) -> Pixel { Pixel { x, y } }
    fn add(&self, pixel: &Pixel) -> Pixel { Pixel::new(self.x + pixel.x, self.y + pixel.y) }
    fn to_idx(&self) -> usize { (self.y as usize * WIDTH) + self.x as usize }
}

struct Vec2d { x: f64, y: f64 }

impl Vec2d {
    const fn new(x: f64, y: f64) -> Vec2d { Vec2d { x, y } }
    fn to_pixel(&self) -> Pixel { Pixel::new(self.x as i32, self.y as i32) }
}

struct Spaceship {
    center: Vec2d,
    velocity: Vec2d,
    pixels: [Pixel; 4],
}

impl Spaceship {
    const fn new() -> Spaceship {
        let pixels = [
            Pixel::new(0, 0),
            Pixel::new(-1, 0),
            Pixel::new(1, 0),
            Pixel::new(0, -1),
        ];
        Spaceship {
            center: Vec2d::new(31.0, 31.0),
            velocity: Vec2d::new(0.0, 0.0),
            pixels
        }
    }

    fn update(&mut self, elapsed: f64, graphics: &mut Graphics) {
        // erase current object pixels
        let world_pixel = self.center.to_pixel();
        for pixel in self.pixels.iter() {
            let idx = world_pixel.add(pixel).to_idx();
            graphics[idx] = 0;
        }

        // update the current point based on velocity and time elapsed
        let new_x = self.velocity.x * elapsed + self.center.x;
        let new_x = if new_x < 1.0 { 1.0 } else { new_x };
        let new_x = if new_x >= (WIDTH - 2) as f64 { (WIDTH - 2) as f64 } else { new_x };
        self.center = Vec2d::new(new_x, self.center.y);

        // re-draw object based on new position
        let world_pixel = self.center.to_pixel();
        for pixel in self.pixels.iter() {
            let idx = world_pixel.add(pixel).to_idx();
            graphics[idx] = 1;
        }
    }

    fn key_pressed(&mut self, key_code: &str) {
        if key_code == "ArrowRight" {
            self.velocity.x = 0.05;
        } else if key_code == "ArrowLeft" {
            self.velocity.x = -0.05;
        } else {
            // noop
        }
    }
}

struct Game {
    spaceship: Spaceship,
    graphics: Graphics,
}

impl Game {
    const fn new() -> Game {
        Game {
            spaceship: Spaceship::new(),
            graphics: [0; WIDTH * HEIGHT]
        }
    }

    fn update(&mut self, elapsed: f64) {
        self.spaceship.update(elapsed, &mut self.graphics);
    }

    fn key_pressed(&mut self, key_code: &str) {
        self.spaceship.key_pressed(key_code);
    }
}

static mut GAME: Game = Game::new();

#[wasm_bindgen]
extern "C" {
    #[wasm_bindgen(js_namespace = console)]
    fn log(s: &str);
}

#[wasm_bindgen]
pub fn game_start() {
    log("game is starting");
    set_panic_hook();
    unsafe {
        GAME = Game::new();
        GAME.update(0.0);
    }
}

#[wasm_bindgen]
pub fn game_tick(elapsed: f64) {
    unsafe {
        GAME.update(elapsed);
    }
}

#[wasm_bindgen]
pub fn game_key_pressed(key_code: &str) {
    unsafe {
        GAME.key_pressed(key_code);
    }
}

#[wasm_bindgen]
pub fn game_graphics_pointer() -> *const u8 {
    let pointer: *const u8;
    unsafe {
        pointer = GAME.graphics.as_ptr();
    }
    pointer
}
