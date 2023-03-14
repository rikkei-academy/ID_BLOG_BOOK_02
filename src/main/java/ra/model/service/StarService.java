package ra.model.service;

import ra.model.entity.Star;
import ra.payload.request.StarRequest;

public interface StarService extends RootService<Star,Integer>{
    Star mapRequestToStar(StarRequest request);
}
