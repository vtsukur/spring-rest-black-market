/*
  global module
  */

module.exports = function(grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON("package.json"),
        browserify: {
            "main.js": ["js/admin.js"],
            options: {
                browserifyOptions: {
                    debug: true
                }
            }
        },
        watch: {
            files: ["js/**/*.js"],
            tasks: ["browserify"]
        }
    });
    grunt.loadNpmTasks("grunt-contrib-watch");
    grunt.loadNpmTasks("grunt-browserify");
};