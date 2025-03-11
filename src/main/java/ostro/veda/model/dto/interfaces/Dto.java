package ostro.veda.model.dto.interfaces;

public interface Dto {

    String toJSON();

    default Dto fromJSON(String json) {
        return null;
    }
}
