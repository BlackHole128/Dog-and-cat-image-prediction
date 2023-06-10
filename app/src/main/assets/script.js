// set up PIXI //
let width = window.innerWidth;
let height = window.innerHeight;

let particles = [];

let app = new PIXI.Application({
  width: width, // default: 800
  height: height, // default: 600
  antialias: true, // default: false
  transparent: true, // default: false
  resolution: 1 // default: 1
});

let count = 0;
let max = 60;
let timer = gsap.to({}, {
  onComplete: placeEmitter,
  duration: 0.03
});
let colors = [0xff3399, 0xffffff, 0x6666ff, 0x33ffff];

gsap.set(app.stage, {
  pixi: {
    blurX: 2,
    blurY: 2
  }
});

document.getElementById("animation-container").appendChild(app.view);

class Emitter extends PIXI.Container {
  constructor(numCircles, color) {
    super();
    this.animation = gsap.timeline();
    this.circles = [];
    this.containers = [];
    this.startX = 70;
    for (let i = 0; i < numCircles; i++) {
      let container = new PIXI.Container();
      let circle = new PIXI.Graphics();
      circle.beginFill(color);
      circle.drawCircle(0, 0, 2);
      circle.pivot.x = 0;
      circle.pivot.y = 0;
      circle.endFill();
      container.x = this.startX;
      this.containers.push(container);
      this.circles.push(circle);
      container.addChild(circle);
      this.addChild(container);
    }
    var distributor = gsap.utils.distribute({
      base: 0,
      amount: 500,
      from: "default",
      ease: "power2.in"
    });
    let xDistance = gsap.utils.distribute({
      base: -width / 2,
      amount: width,
      ease: "power1.out"
    });

    let scale = gsap.utils.distribute({
      base: 1.2,
      amount: 15,
      ease: "power1.in"
    });

    gsap.set(this.circles, {
      x: distributor,
      pixi: {
        scale: scale
      }
    });

    this.animation
      .from(this.circles, {
        alpha: 0,
        pixi: {
          scale: 0.1
        },
        duration: 0.8,
        stagger: {
          each: 0.08
        }
      })
      .to(
        this.containers, {
          x: function (index, element, target) {
            return "+=" + xDistance(index, element, target);
          },
          duration: 7,
          ease: "linear"
        },
        0
      )
      .to(
        this.circles, {
          alpha: 0,
          duration: 5,
          pixi: {
            scale: 0
          },
          ease: "power2"
        },
        "-=5"
      )
      .pause();
  }
}

function weightedRandom(collection, ease) {
  return gsap.utils.pipe(
    Math.random, //random number between 0 and 1
    gsap.parseEase(ease), //apply the ease
    gsap.utils.mapRange(0, 1, 0, collection.length - 1), //map to the index range of the array
    gsap.utils.snap(1), //snap to the closest integer
    i => collection[i] //return that element from the array
  );
}

// usage:
var getRandom = weightedRandom(colors, "power2.in");

for (var n = 0; n < max; n++) {
  let e = new Emitter(gsap.utils.random(16, 24), getRandom());
  particles.push(e);
}

var rotation = 0;

function placeEmitter() {
  let angle = count * 0.2; // Adjust the angle increment for the desired spiral effect
  let radius = count * 2; // Adjust the radius increment for the desired spiral effect

  let posX = -width / 2 + Math.cos(angle) * radius + width / 2;
  let posY = Math.sin(angle) * radius + height / 2;

  let e = app.stage.addChild(particles[count % particles.length]);
  e.position.set(posX, posY);
  e.animation.restart();

  gsap.set(e, {
    pixi: {
      rotation: (rotation += gsap.utils.random(10, 14))
    }
  });

  count++;
  timer.restart();
}

window.addEventListener("resize", function () {
  width = window.innerWidth;
  height = window.innerHeight;
  app.renderer.resize(width, height);
  placeEmitter();
});

placeEmitter();
