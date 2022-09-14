import init, { game_start, game_tick } from "/pkg/the_daily_game.js"

async function bootstrap() {
    await init();
    game_start();

    let game_ctx = {
        last_tick: performance.now()
    };

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
    // TODO:
}
