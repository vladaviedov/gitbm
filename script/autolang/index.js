const minimist = require("minimist");
const os = require("os");
const fs = require("fs");

const fatal = msg => {
	console.error(msg);
	process.exit(1);
};

let location = os.homedir() + "/.minecraft/";
let lang = undefined;
let mcversion = undefined;

// Parse arguments
const argv = minimist(process.argv.slice(2));
for (const arg in argv) {
	switch (arg) {
		case "location":
			location = argv[arg];
			break;
		case "lang":
			lang = argv[arg];
			break;
		case "mcversion":
			mcversion = argv[arg];
			break;
		case "_":
			break;
		default:
			fatal("Unknown argument: ", arg);
			break;
	}
}

// Check arguments
if (lang == undefined)
	fatal("Error: 'lang' must be specified");
if (mcversion == undefined)
	fatal("Error: 'mcversion' must be specified");

// Get indexes file
const indexesPath = location + "assets/indexes/" + mcversion + ".json";
let indexes;
try {
	indexes = JSON.parse(fs.readFileSync(indexesPath, "utf-8"));
} catch (err) {
	fatal("Failed to load lang file\n" + err);
}

// Obtain lang file hash
const langIndexKey = "minecraft/lang/" + lang + ".json";
const langEntry = indexes["objects"][langIndexKey];
if (langEntry == undefined)
	fatal("Error: language not listed in indexes");
const langHash = langEntry["hash"];

// Get lang file
const langPath = location + "assets/objects/" + langHash.substr(0, 2) + "/" + langHash;
let langFile;
try {
	langFile = JSON.parse(fs.readFileSync(langPath, "utf-8"));
} catch (err) {
	fatal("Failed to load lang file\n" + err);
}

console.log(langFile);