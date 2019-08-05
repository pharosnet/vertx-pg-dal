package org.pharosnet.vertx.pg.dal.core;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;
import org.pharosnet.vertx.pg.dal.core.convert.RowConvert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface AbstractDAL {

    <R> void query(String ql, RowConvert<R> convert, Handler<AsyncResult<Optional<Stream<R>>>> handler);
    <R> void query(String ql, Tuple args, RowConvert<R> convert, Handler<AsyncResult<Optional<Stream<R>>>> handler);
    <R> void queryOne(String ql, RowConvert<R> convert, Handler<AsyncResult<Optional<R>>> handler);
    <R> void queryOne(String ql, Tuple args, RowConvert<R> convert, Handler<AsyncResult<Optional<R>>> handler);
    void queryString(String ql, Tuple args, Handler<AsyncResult<Optional<String>>> handler);
    void queryBoolean(String ql, Tuple args, Handler<AsyncResult<Optional<Boolean>>> handler);
    void queryInteger(String ql, Tuple args, Handler<AsyncResult<Optional<Integer>>> handler);
    void queryLong(String ql, Tuple args, Handler<AsyncResult<Optional<Long>>> handler);
    void queryFloat(String ql, Tuple args, Handler<AsyncResult<Optional<Float>>> handler);
    void queryDouble(String ql, Tuple args, Handler<AsyncResult<Optional<Double>>> handler);
    void queryJsonObject(String ql, Tuple args, Handler<AsyncResult<Optional<JsonObject>>> handler);
    void update(String ql, Handler<AsyncResult<Integer>> handler);
    void update(String ql, Tuple args, Handler<AsyncResult<Integer>> handler);
    void updateBatch(String ql, List<Tuple> args, Handler<AsyncResult<Integer>> handler);
    void update(ExecBuilder builder, Handler<AsyncResult<Integer>> handler);
    void updateBatch(ExecBatchBuilder builder, Handler<AsyncResult<Integer>> handler);
    void tx(Handler<AsyncResult<Tx>> handler);

}
