package pl.pierce.testtask.csv2json.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.pierce.testtask.csv2json.csv.Attribute;
import pl.pierce.testtask.csv2json.csv.Option;
import pl.pierce.testtask.csv2json.jsonservice.JsonService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AttributesController {

    private final JsonService jsonService;

    @GetMapping("/attributes")
    public List<Attribute> getAttributes(HttpServletRequest request,
                                         @RequestParam(name="lang", required=false) String lang) {
        return jsonService.getAttributes(request, lang);
    }

    @GetMapping("/attributes/{id}")
    public Attribute getSingleAttribute(@PathVariable String id,
                                        HttpServletRequest request,
                                        @RequestParam(name="lang", required=false) String lang) {
        return jsonService.getSingleAttribute(id, request, lang);
    }

    @GetMapping("/attributes/{id}/options")
    public Attribute getAttributeWithOptions(@PathVariable String id,
                                        HttpServletRequest request,
                                        @RequestParam(name="lang", required=false) String lang) {
        return jsonService.getAttributeWithOptions(id, request, lang);
    }

    @PostMapping("/attributes/")
    public Attribute postAttribute(@RequestBody Attribute attribute) {
        return jsonService.postAttribute(attribute);
    }

    @PostMapping("/attributes/{id}/options")
    public Option postAttributesOption(@PathVariable String id,
                                       @RequestBody Option option) {
        return jsonService.postAtributesOption(id, option);
    }

    @DeleteMapping("/attributes/{id}")
    public void deleteAttribute(@PathVariable String id) {
        jsonService.deleteAttribute(id);
    }

    @DeleteMapping("/attributes/{id}/options/{optionId}")
    public void deleteAttributesOption(@PathVariable String id,
                             @PathVariable String optionId) {
        jsonService.deleteAttributesOption(id, optionId);
    }
}
