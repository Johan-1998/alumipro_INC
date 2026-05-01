const fs = require("fs");
const path = require("path");
const target = path.join(__dirname, "..", "dist-test", "package.json");
fs.mkdirSync(path.dirname(target), { recursive: true });
fs.writeFileSync(target, JSON.stringify({ type: "commonjs" }, null, 2));
