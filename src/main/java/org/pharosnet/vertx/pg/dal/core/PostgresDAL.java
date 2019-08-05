package org.pharosnet.vertx.pg.dal.core;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Transaction;
import io.vertx.sqlclient.Tuple;
import org.pharosnet.vertx.pg.dal.core.convert.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostgresDAL implements AbstractDAL {

    private static final Logger log = LoggerFactory.getLogger(PostgresDAL.class);

    private static PostgresDAL dal;

    public static PostgresDAL INTI(PgPool client) {
        if (client == null) {
            throw new IllegalArgumentException("client is null point");
        }
        dal = new PostgresDAL(client);
        return dal;
    }

    public static PostgresDAL get() {
        if (dal == null) {
            throw new IllegalAccessError("dal is null point");
        }
        return dal;
    }

    public PostgresDAL(PgPool client) {
        this.client = client;
    }

    private PgPool client;

    public PgPool getClient() {
        return client;
    }

    public <R> void query(String ql, RowConvert<R> convert, Handler<AsyncResult<Optional<Stream<R>>>> handler) {
        this.client.query(
                ql,
                Collectors.mapping(
                        convert::convert,
                        Collectors.toList()
                ),
                r -> {
                    if (r.failed()) {
                        log.error("query failed", r.cause());
                        if (log.isDebugEnabled()) {
                            log.debug("query:\n\tsql:{}", ql);
                        }
                        handler.handle(Future.failedFuture(r.cause()));
                        return;
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("query:\n\tsql:{}\n\trows:{}", ql, r.result().rowCount());
                    }
                    if (r.result().rowCount() == 0) {
                        handler.handle(Future.succeededFuture(Optional.empty()));
                        return;
                    }
                    handler.handle(Future.succeededFuture(Optional.of(r.result().value().stream())));
                });
    }

    public <R> void query(String ql, Tuple args, RowConvert<R> convert, Handler<AsyncResult<Optional<Stream<R>>>> handler) {
        this.client.preparedQuery(
                ql,
                args,
                Collectors.mapping(
                        convert::convert,
                        Collectors.toList()
                ),
                r -> {
                    if (r.failed()) {
                        log.error("query failed", r.cause());
                        if (log.isDebugEnabled()) {
                            log.debug("query:\n\tsql:{}\n\targs:{}", ql, args.toString());
                        }
                        handler.handle(Future.failedFuture(r.cause()));
                        return;
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("query:\n\tsql:{}\n\targs:{}\n\trows:{}", ql, args.toString(), r.result().rowCount());
                    }
                    if (r.result().rowCount() == 0) {
                        handler.handle(Future.succeededFuture(Optional.empty()));
                        return;
                    }
                    handler.handle(Future.succeededFuture(Optional.of(r.result().value().stream())));
                });
    }

    public <R> void queryOne(String ql, RowConvert<R> convert, Handler<AsyncResult<Optional<R>>> handler) {
        this.client.query(
                ql,
                Collectors.mapping(
                        convert::convert,
                        Collectors.toList()
                ),
                r -> {
                    if (r.failed()) {
                        log.error("query one failed", r.cause());
                        if (log.isDebugEnabled()) {
                            log.debug("query one:\n\tsql:{}", ql);
                        }
                        handler.handle(Future.failedFuture(r.cause()));
                        return;
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("query one:\n\tsql:{}\n\trows:{}", ql, r.result().rowCount());
                    }
                    if (r.result().rowCount() == 0) {
                        handler.handle(Future.succeededFuture(Optional.empty()));
                        return;
                    }
                    handler.handle(Future.succeededFuture(Optional.of(r.result().value().get(0))));
                });
    }

    public <R> void queryOne(String ql, Tuple args, RowConvert<R> convert, Handler<AsyncResult<Optional<R>>> handler) {
        this.client.preparedQuery(
                ql,
                args,
                Collectors.mapping(
                        convert::convert,
                        Collectors.toList()
                ),
                r -> {
                    if (r.failed()) {
                        log.error("query one failed", r.cause());
                        if (log.isDebugEnabled()) {
                            log.debug("query one:\n\tsql:{}\n\targs:{}", ql, args.toString());
                        }
                        handler.handle(Future.failedFuture(r.cause()));
                        return;
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("query one:\n\tsql:{}\n\targs:{}\n\trows:{}", ql, args.toString(), r.result().rowCount());
                    }
                    if (r.result().rowCount() == 0) {
                        handler.handle(Future.succeededFuture(Optional.empty()));
                        return;
                    }
                    handler.handle(Future.succeededFuture(Optional.of(r.result().value().get(0))));
                });
    }

    public void queryString(String ql, Tuple args, Handler<AsyncResult<Optional<String>>> handler) {
        this.queryOne(ql, args, RowConverts.stringRowConvert(), handler);
    }

    public void queryBoolean(String ql, Tuple args, Handler<AsyncResult<Optional<Boolean>>> handler) {
        this.queryOne(ql, args, RowConverts.booleanRowConvert(), handler);
    }

    public void queryInteger(String ql, Tuple args, Handler<AsyncResult<Optional<Integer>>> handler) {
        this.queryOne(ql, args, RowConverts.integerRowConvert(), handler);
    }

    public void queryLong(String ql, Tuple args, Handler<AsyncResult<Optional<Long>>> handler) {
        this.queryOne(ql, args, RowConverts.longRowConvert(), handler);
    }

    public void queryFloat(String ql, Tuple args, Handler<AsyncResult<Optional<Float>>> handler) {
        this.queryOne(ql, args, RowConverts.floatRowConvert(), handler);
    }

    public void queryDouble(String ql, Tuple args, Handler<AsyncResult<Optional<Double>>> handler) {
        this.queryOne(ql, args, RowConverts.doubleRowConvert(), handler);
    }

    public void queryJsonObject(String ql, Tuple args, Handler<AsyncResult<Optional<JsonObject>>> handler) {
        this.queryOne(ql, args, RowConverts.jsonObjectRowConvert(), handler);
    }

    public void update(String ql, Handler<AsyncResult<Integer>> handler) {
        this.client.query(
                ql,
                r -> {
                    if (r.failed()) {
                        log.error("update failed", r.cause());
                        if (log.isDebugEnabled()) {
                            log.debug("update:\n\tsql:{}", ql);
                        }
                        handler.handle(Future.failedFuture(r.cause()));
                        return;
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("update:\n\tsql:{}\n\trows:{}", ql, r.result().rowCount());
                    }
                    handler.handle(Future.succeededFuture(r.result().rowCount()));
                }
        );
    }

    public void update(String ql, Tuple args, Handler<AsyncResult<Integer>> handler) {
        this.client.preparedQuery(
                ql,
                args,
                r -> {
                    if (r.failed()) {
                        log.error("update failed", r.cause());
                        if (log.isDebugEnabled()) {
                            log.debug("update:\n\tsql:{}\n\targs:{}", ql, args.toString());
                        }
                        handler.handle(Future.failedFuture(r.cause()));
                        return;
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("update:\n\tsql:{}\n\targs:{}\n\trows:{}", ql, args.toString(), r.result().rowCount());
                    }
                    handler.handle(Future.succeededFuture(r.result().rowCount()));
                }
        );
    }

    public void updateBatch(String ql, List<Tuple> args, Handler<AsyncResult<Integer>> handler) {
        this.client.preparedBatch(
                ql,
                args,
                r -> {
                    if (r.failed()) {
                        log.error("update batch failed", r.cause());
                        if (log.isDebugEnabled()) {
                            log.debug("update batch:\n\tsql:{}\n\targs:{}", ql, args.toString());
                        }
                        handler.handle(Future.failedFuture(r.cause()));
                        return;
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("update batch:\n\tsql:{}\n\targs:{}\n\trows:{}", ql, args.toString(), r.result().rowCount());
                    }
                    handler.handle(Future.succeededFuture(r.result().rowCount()));
                }
        );
    }

    public void update(ExecBuilder builder, Handler<AsyncResult<Integer>> handler) {
        this.update(builder.query(), builder.args(), handler);
    }

    public void updateBatch(ExecBatchBuilder builder, Handler<AsyncResult<Integer>> handler) {
        this.updateBatch(builder.query(), builder.args(), handler);
    }

    public void tx(Handler<AsyncResult<Tx>> handler) {
        this.client.begin(r -> {
            if (r.failed()) {
                log.error("tx begin failed", r.cause());
                handler.handle(Future.failedFuture(r.cause()));
                return;
            }
            Transaction transaction = r.result();
            handler.handle(Future.succeededFuture(new Tx(transaction)));
        });
    }

}
