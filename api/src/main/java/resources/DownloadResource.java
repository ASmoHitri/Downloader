package resources;

import beans.SongFileBean;
import com.kumuluz.ee.logs.cdi.Log;
import configs.AppConfigs;
import dtos.SongFile;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Log
@ApplicationScoped
@Path("/download")
@Produces(MediaType.APPLICATION_JSON)
public class DownloadResource {
    @Inject
    private SongFileBean songBean;


    @Inject
    private AppConfigs appConfigs;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getSongFile(@PathParam("id") Integer songId) {
        SongFile songFile = songBean.getSong(songId);
        if (songFile == null) {
            return  Response.status(Response.Status.NOT_FOUND).build();
        }
        String path = songFile.getFilePath() + songFile.getFileName();
        if (appConfigs.getOs().equals("linux")) {
            path = "/files" + path;
        }
        byte[] out = songBean.getSongFile(path);
        System.out.println("length " + out.length);
        return Response.ok(out).build();
    }
}
