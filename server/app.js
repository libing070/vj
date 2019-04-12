var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var bodyParser = require('body-parser');
var session = require('express-session');
var ejs=require('ejs');
var indexRouter = require('./routes/index');
// const logger = require('./middleware/logger')
// const log4js = require('./utils/logger')
// const log = log4js.getLogger('app.js')


var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.engine('.html',ejs.__express);//使用html
//app.set('view engine', 'jade');
app.set('view engine', 'html');
// 控制台日志输出
// app.use(logger);

app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use(session({
  resave: false,
  saveUninitialized: false,
  cookie: {maxAge: 24 * 60 * 60 * 1000},  //设置maxAge是1天，即1天后session和相应的cookie失效过期
  secret: 'love'
}));


app.use('/', indexRouter.router);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;

// log.info('服务启动....')
