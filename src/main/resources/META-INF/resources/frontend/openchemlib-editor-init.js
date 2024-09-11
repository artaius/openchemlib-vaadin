import OCL from 'openchemlib/full.pretty.js';

try {
    console.info("Registering OCL custom tags...");
    OCL.registerCustomElement();
    console.info("Registered OCL custom tags.");
} catch (error) {
    console.error(error);
}





