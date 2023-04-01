package edu.usc.nlcaceres.infectionprotection_backend.controllers.graphql;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.controllers.LocationGraphController;
import edu.usc.nlcaceres.infectionprotection_backend.graphql_scalars.DateScalarConfiguration;
import edu.usc.nlcaceres.infectionprotection_backend.models.Location;
import edu.usc.nlcaceres.infectionprotection_backend.services.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import java.util.List;
import static org.mockito.Mockito.when;

@GraphQlTest(LocationGraphController.class)
@Import(DateScalarConfiguration.class)
public class LocationControllerTests {

    @Autowired
    private GraphQlTester tester;

    @MockBean
    private LocationService locationService;

    @Test
    public void getLocationList() throws Exception {
        List<Location> mockLocationList = ModelFactory.getLocationList();
        when(locationService.getAll()).thenReturn(mockLocationList);

        this.tester.document("{ locations { id facilityName unitNum roomNum } }").execute()
                .path("locations").entityList(Location.class).hasSize(2);
    }

    @Test
    public void getLocationById() throws Exception {
        Location mockLocation = ModelFactory.getLocation(null, null, null);
        when(locationService.getById("abc")).thenReturn(mockLocation);

        // Another option using matchesJson() is Java 15 Text blocks that handle indentation and newlines super well!
        // Especially by comparison to old school "+", concat(), join(), or StringWriter/Builder
        // HOWEVER, Text blocks see every line as a whole new line so if you have a really long phrase then use the backslash!
        // """I will write something really super long \
        // and this will print out on the exact same line!
        // BUT this will appear on a different line!"""
        this.tester.document("{ locationById(id: \"abc\") { facilityName unitNum roomNum } }").execute().path("locationById")
                .matchesJson("""
                    {
                        "facilityName": "USC",
                        "unitNum": "2",
                        "roomNum": "123"
                    }
                """);
    }
    @Test
    public void tryToGetUnknownLocation() throws Exception {
        when(locationService.getById("123")).thenThrow(new RuntimeException());
        this.tester.document("{ locationById(id: \"123\") { facilityName } }").execute().path("locationById")
                .valueIsNull();
    }
}
