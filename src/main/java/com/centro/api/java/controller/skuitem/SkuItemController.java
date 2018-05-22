package com.centro.api.java.controller.skuitem;


import com.centro.api.java.models.skuitem.SkuItemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/skuitem")
public class SkuItemController {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createSkuItem(@RequestBody()SkuItemDetail skuItemDetail) throws Exception {
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/{skuItemId}",method = RequestMethod.GET)
    public ResponseEntity<SkuItemDetail> getSkuItemDetail(@PathVariable("skuItemId") String skuItemId) throws Exception {
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<?> findSkuItem(@RequestParam("searchQuery") String searchQuery) throws Exception {
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/{skuItemId}", method =RequestMethod.PUT)
    public ResponseEntity<?> updateSkuItem(@PathVariable("skuItemId") String skuItemId, @RequestBody() SkuItemDetail skuItemDetail) {
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/{skuItemId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSkuItem(@PathVariable("skuItemId") String skuItemId) throws Exception {
        return ResponseEntity.ok(null);
    }

}
