package br.com.oficina.shared.application.port;

import java.util.function.Supplier;

public interface UnitOfWork {

    <T> T execute(Supplier<T> work);
}
