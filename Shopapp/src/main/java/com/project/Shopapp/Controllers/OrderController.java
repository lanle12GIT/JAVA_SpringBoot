package com.project.Shopapp.Controllers;

import com.project.Shopapp.dtos.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            return ResponseEntity.ok("Order Successfully");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{user_id}")
    public ResponseEntity<?> getOrder( @Valid @PathVariable("user_id") Long userId) {
        try {
            return ResponseEntity.ok(" Get order from userId: "+ userId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder( @Valid @PathVariable Long id,
         @Valid @RequestBody OrderDTO orderDTO ){

            return ResponseEntity.ok("This is updateOrder with id ="+ id);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder( @Valid @PathVariable Long id){
        //xóa mềm => cập nhật trường active= false
        return ResponseEntity.ok("Order deleted successfully with id ="+ id);
    }

}
