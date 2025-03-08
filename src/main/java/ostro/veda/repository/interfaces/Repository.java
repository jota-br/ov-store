package ostro.veda.repository.interfaces;

public interface Repository<T> extends AutoCloseable {

    T add(T t);

    T update(T t);
}
