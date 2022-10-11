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

#[derive(Clone)]
struct Vec2d { x: f64, y: f64 }

impl Vec2d {
    const fn new(x: f64, y: f64) -> Vec2d { Vec2d { x, y } }
    fn to_pixel(&self) -> Pixel { Pixel::new(self.x as i32, self.y as i32) }
}

struct Missile {
    center: Vec2d,
    velocity: Vec2d,
    pixels: [Pixel; 2],
}

impl Missile {
    fn new(center: Vec2d) -> Missile {
        Missile {
            center,
            velocity: Vec2d::new(0.0, -0.01),
            pixels: [Pixel::new(0, 0), Pixel::new(0, -1)]
        }
    }
}

struct Spaceship {
    center: Vec2d,
    velocity: Vec2d,
    pixels: [Pixel; 4],
    missiles: [Option<Missile>; 5],
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
            pixels,
            missiles: [None, None, None, None, None]
        }
    }

    fn update(&mut self, elapsed: f64, graphics: &mut Graphics) {
        self.update_spaceship(elapsed, graphics);
        self.update_missiles(elapsed, graphics);
    }

    fn update_spaceship(&mut self, elapsed: f64, graphics: &mut Graphics) {
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

    fn update_missiles(&mut self, elapsed: f64, graphics: &mut Graphics) {
        for missile in self.missiles.iter_mut() {
            if let Some(m) = missile {
                // update the current point based on velocity and time elapsed
                let min_height = 1 as f64;
                let new_y = m.velocity.y * elapsed + m.center.y;
                if new_y < min_height { *missile = None; }
                else { m.center = Vec2d::new(m.center.x, new_y); };
            }
        }

        // re-draw objects based on new position
        for missile in self.missiles.iter() {
            if let Some(m) = missile {
                let world_pixel = m.center.to_pixel();
                for pixel in m.pixels.iter() {
                    let idx = world_pixel.add(pixel).to_idx();
                    graphics[idx] = 1;
                }
            }
        }
    }

    fn fire(&mut self) {
        for missile in self.missiles.iter_mut() {
            if let None = missile {
                *missile = Some(Missile::new(self.center.clone()));
                break;
            }
        }
    }

    fn key_pressed(&mut self, key_code: &str) {
        if key_code == "ArrowRight" {
            self.velocity.x = 0.05;
        } else if key_code == "ArrowLeft" {
            self.velocity.x = -0.05;
        } else if key_code == "Space" {
            self.fire();
        } else {
            // noop
        }
    }
}

struct Alien {
    center: Vec2d,
    velocity: Vec2d,
    pixels: [Pixel; 46],
    energy: i32,
}

impl Alien {
    const fn new() -> Alien {
        let pixels = [
            Pixel::new(0, 0),

            // center line
            Pixel::new(-1, 0),
            Pixel::new(-2, 0),
            Pixel::new(-3, 0),
            Pixel::new(-5, 0),
            Pixel::new(1, 0),
            Pixel::new(2, 0),
            Pixel::new(3, 0),
            Pixel::new(5, 0),

            // below center
            Pixel::new(-5, 1),
            Pixel::new(-3, 1),
            Pixel::new(3, 1),
            Pixel::new(5, 1),

            // 2 lines below center
            Pixel::new(-1, 2),
            Pixel::new(-2, 2),
            Pixel::new(1, 2),
            Pixel::new(2, 2),

            // above center
            Pixel::new(-5, -1),
            Pixel::new(-4, -1),
            Pixel::new(-3, -1),
            Pixel::new(-2, -1),
            Pixel::new(-1, -1),
            Pixel::new(0, -1),
            Pixel::new(5, -1),
            Pixel::new(4, -1),
            Pixel::new(3, -1),
            Pixel::new(2, -1),
            Pixel::new(1, -1),

            // alien eye line
            Pixel::new(-4, -2),
            Pixel::new(-3, -2),
            Pixel::new(-1, -2),
            Pixel::new(0, -2),
            Pixel::new(1, -2),
            Pixel::new(3, -2),
            Pixel::new(4, -2),

            // above alien eye
            Pixel::new(-3, -3),
            Pixel::new(-2, -3),
            Pixel::new(-1, -3),
            Pixel::new(-0, -3),
            Pixel::new(1, -3),
            Pixel::new(2, -3),
            Pixel::new(3, -3),

            // ears
            Pixel::new(-2, -4),
            Pixel::new(-3, -5),
            Pixel::new(2, -4),
            Pixel::new(3, -5),
        ];
        Alien {
            center: Vec2d::new(31.0, 10.0),
            velocity: Vec2d::new(0.0, 0.001),
            pixels,
            energy: 100,
        }
    }

    fn update(&mut self, elapsed: f64, graphics: &mut Graphics) {
        let max_height = self.max_height();
        let new_y = self.velocity.y * elapsed + self.center.y;
        let new_y = if new_y >= max_height { max_height } else { new_y };
        self.center = Vec2d::new(self.center.x, new_y);

        // re-draw object based on new position
        let world_pixel = self.center.to_pixel();
        for pixel in self.pixels.iter() {
            let idx = world_pixel.add(pixel).to_idx();
            graphics[idx] = 1;
        }
    }

    fn max_height(&self) -> f64 { (HEIGHT - 5) as f64 }

    fn in_the_ground(&self) -> bool { self.center.y >= self.max_height() }

    fn is_destroyed(&self) -> bool { self.energy <= 0 }
}

struct Game {
    spaceship: Spaceship,
    alien: Alien,
    graphics: Graphics,
}

impl Game {
    const fn new() -> Game {
        Game {
            spaceship: Spaceship::new(),
            alien: Alien::new(),
            graphics: [0; WIDTH * HEIGHT],
        }
    }

    fn update(&mut self, elapsed: f64) {
        self.erase_graphics();
        self.alien.update(elapsed, &mut self.graphics);
        self.spaceship.update(elapsed, &mut self.graphics);

        // restart game if the alien hit the ground or if the alien has been destroyed
        if self.alien.in_the_ground() { self.restart(); }
        if self.alien.is_destroyed() { self.restart(); }
    }

    fn erase_graphics(&mut self) {
        for i in 0..self.graphics.len() { self.graphics[i] = 0; }
    }

    fn key_pressed(&mut self, key_code: &str) {
        self.spaceship.key_pressed(key_code);
    }

    fn restart(&mut self) {
        log("game is restarting");
        self.spaceship = Spaceship::new();
        self.alien = Alien::new();
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
