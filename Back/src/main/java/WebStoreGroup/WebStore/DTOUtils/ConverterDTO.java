package WebStoreGroup.WebStore.DTOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Component;

@Component
public class ConverterDTO {
    ProjectionFactory pf;

    @Autowired
    public ConverterDTO(ProjectionFactory pf) {
        this.pf = pf;
    }


    public <T,V> T convert(Class<T> type, V obj)
    {
        return pf.createProjection(type, obj);
    }

}
