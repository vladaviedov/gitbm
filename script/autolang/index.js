const minimist = require("minimist");
const os = require("os");
const fs = require("fs");

const fatal = msg => {
	console.error(msg);
	process.exit(1);
};
const nthIndexOf = (string, substr, n) => {
	let ctr = 0;
	let index = -1;
	do {
		index = string.indexOf(substr, index + 1);
	} while (index != -1 && (++ctr < n));
	return index;
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
const langPath = location + "assets/objects/" + langHash.substring(0, 2) + "/" + langHash;
let langFile;
try {
	langFile = JSON.parse(fs.readFileSync(langPath, "utf-8"));
} catch (err) {
	fatal("Failed to load lang file\n" + err);
}

// Load lang file template
const langTemplate = JSON.parse(fs.readFileSync("../../template/lang.json"));

// Create item name template
const bucketOfEnt = langFile["item.minecraft.pufferfish_bucket"];
const ent = langFile["entity.minecraft.pufferfish"];
const nameTemplate = bucketOfEnt.replace(ent, "%");

// Generate item names
for (let entry in langTemplate) {
	const name = entry.substring(nthIndexOf(entry, "_", 2) + 1);
	if (name == "entity") continue;
	const entity = langFile["entity.minecraft." + name];
	console.log(nameTemplate.replace("%", entity));
}