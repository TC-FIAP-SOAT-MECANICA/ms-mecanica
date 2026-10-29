package br.com.oficina.application.gateways.shared;

import java.util.function.Supplier;

public interface UnitOfWork {

    <T> T execute(Supplier<T> work);
}
