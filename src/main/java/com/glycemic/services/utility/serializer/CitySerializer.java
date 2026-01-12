package com.glycemic.services.utility.serializer;

import java.io.IOException;
import java.io.Serial;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.glycemic.entity.model.glycemic.City;

public class CitySerializer extends StdSerializer<City>{

	@Serial
    private static final long serialVersionUID = -2124290518222619914L;

	public CitySerializer() {
		this(null);
    }

    public CitySerializer(Class<City> t) {
        super(t);
    }

    @Override
    public void serialize(City items, JsonGenerator generator, SerializerProvider provider) throws IOException {
        HashMap<String,Object> ids = new HashMap<>();
        ids.put("id", items.getId());
        ids.put("name", items.getName());
        ids.put("value", items.getValue());
        generator.writeObject(ids);
    }
}
