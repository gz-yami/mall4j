var gulp = require('gulp');
var $    = require('gulp-load-plugins')();
var path = require('path');
var del  = require('del');

var distPath    = path.resolve('./dist');
var version     = ''; // 版本号
var versionPath = ''; // 版本号路径
var env         = ''; // 运行环境

// 创建版本号(年月日时分)
(function () {
  var d = new Date();
  var yy = d.getFullYear().toString().slice(2);
  var MM = d.getMonth() + 1 >= 10 ? (d.getMonth() + 1) : '0' + (d.getMonth() + 1);
  var DD = d.getDate() >= 10 ? d.getDate() : '0' + d.getDate();
  var h  = d.getHours() >= 10 ? d.getHours() : '0' + d.getHours();
  var mm = d.getMinutes() >= 10 ? d.getMinutes() : '0' + d.getMinutes();
  version = yy + MM + DD + h + mm;
  versionPath = distPath + '/' + version;
})();

// 编译
gulp.task('build', $.shell.task([ 'node build/build.js' ]));

// 创建版本号目录
gulp.task('create:versionCatalog', ['build'], function () {
  return gulp.src(`${distPath}/static/**/*`)
    .pipe(gulp.dest(`${versionPath}/static/`))
});

// 替换${versionPath}/static/js/manifest.js window.SITE_CONFIG.cdnUrl占位变量
gulp.task('replace:cdnUrl', ['create:versionCatalog'], function () {
  return gulp.src(`${versionPath}/static/js/manifest.js`)
    .pipe($.replace(new RegExp(`"${require('./config').build.assetsPublicPath}"`, 'g'), 'window.SITE_CONFIG.cdnUrl + "/"'))
    .pipe(gulp.dest(`${versionPath}/static/js/`))
});

// 替换${versionPath}/static/config/index-${env}.js window.SITE_CONFIG['version']配置变量
gulp.task('replace:version', ['create:versionCatalog'], function () {
  return gulp.src(`${versionPath}/static/config/index-${env}.js`)
    .pipe($.replace(/window.SITE_CONFIG\['version'\] = '.*'/g, `window.SITE_CONFIG['version'] = '${version}'`))
    .pipe(gulp.dest(`${versionPath}/static/config/`))
});

// 合并${versionPath}/static/config/[index-${env}, init].js 至 ${distPath}/config/index.js
gulp.task('concat:config', ['replace:version'], function () {
  return gulp.src([`${versionPath}/static/config/index-${env}.js`, `${versionPath}/static/config/init.js`])
    .pipe($.concat('index.js'))
    .pipe(gulp.dest(`${distPath}/config/`))
});

// 清空
gulp.task('clean', function () {
  return del([versionPath])
});

gulp.task('default', ['clean'], function () {
  // 获取环境配置
  env = process.env.npm_config_qa ? 'qa' : process.env.npm_config_uat ? 'uat' : 'prod'
  // 开始打包编译
  gulp.start(['build', 'create:versionCatalog', 'replace:cdnUrl', 'replace:version', 'concat:config'], function () {
    // 清除, 编译 / 处理项目中产生的文件
    del([`${distPath}/static`, `${versionPath}/static/config`])
  })
});
