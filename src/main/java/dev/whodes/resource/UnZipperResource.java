package dev.whodes.resource;


import dev.whodes.dto.ZipperResponse;
import dev.whodes.service.UnZipperService;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.File;

@Path("api/unzip")
public class UnZipperResource {

    @Inject
    UnZipperService unZipperService;

    private static final Logger logger = LoggerFactory.getLogger(UnZipperService.class);


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unzipFile(@FormParam("file") File file) {
        try {
            String uuid = unZipperService.unzip(file);
            return Response.ok(new ZipperResponse(uuid)).build();
        } catch (Exception e) {
            logger.error(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error unzipping file: " + e.getMessage())
                    .build();
        }

    }
}
