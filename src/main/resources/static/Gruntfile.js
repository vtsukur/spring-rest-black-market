/*
  global module
  */

module.exports = function(grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON("package.json"),
        browserify: {
            "admin.js": ["js/admin.js"],
            "index.js": ["js/index.js"],
            options: {
                browserifyOptions: {
                    debug: true
                }
            }
        },
        watch: {
            files: ["js/**/*.js", "!**/node_modules/**"],
            tasks: ["browserify"]
        }
    });
    grunt.loadNpmTasks("grunt-contrib-watch");
    grunt.loadNpmTasks("grunt-browserify");
    grunt.registerTask('default', ['browserify']);
};