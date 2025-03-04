package ostro.veda.db;

public interface Repository<T> extends AutoCloseable {

    T add(T t);

    T update(T t);
}
