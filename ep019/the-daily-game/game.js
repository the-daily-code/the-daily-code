import init, { game_start, game_tick, game_graphics_pointer, game_key_pressed } from "/pkg/the_daily_game.js"

// export to wasm
const WIDTH = 64;
const HEIGHT = 32;

async function bootstrap() {
    let wasm = await init();
    game_start();
    let game_ctx = {
        wasm: wasm,
        last_tick: performance.now(),
        canvas: document.getElementById("game")
    };

    window.addEventListener("keydown", (e) => { game_key_pressed(e.code) });
    window.requestAnimationFrame(function(timestamp) {
        loop(timestamp, game_ctx);
    });
}

bootstrap();

function loop(timestamp, game_ctx) {
    let elapsed = timestamp - game_ctx.last_tick;

    // let the game update its state
    game_tick(elapsed);

    // draw
    draw(game_ctx);

    // schedule next frame
    game_ctx.last_tick = performance.now();
    window.requestAnimationFrame(function(timestamp) {
        loop(timestamp, game_ctx)
    });
}

function draw(game_ctx) {
    const ctx = game_ctx.canvas.getContext('2d');
    ctx.clearRect(0, 0, WIDTH * 10, HEIGHT * 10);
    const pixels = WIDTH * HEIGHT;
    const mem_ptr = game_graphics_pointer();
    const mem = new Uint8Array(game_ctx.wasm.memory.buffer);
    // we represent each game pixel as 10x10 pixels on the canvas
    for (let i = 0; i < pixels; i++) {
        let pixel_lit = mem[mem_ptr + i] > 0;
        if (!pixel_lit) continue;
        let x = (i % WIDTH);
        let y = Math.floor(i / WIDTH);
        ctx.fillStyle = 'rgb(255, 255, 255)';
        ctx.fillRect(x * 10, y * 10, 10, 10);
    }
}
