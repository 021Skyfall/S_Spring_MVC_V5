package IO.SampleWeek3_SpringDataJPA.response;

import IO.SampleWeek3_SpringDataJPA.exception.BusinessLogicException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FieldError> fieldErrors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ConstraintViolationError> violationErrors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BusinessError businessError;

    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(FieldError.of(bindingResult),null,null);
    }

    public static ErrorResponse of(Set<ConstraintViolation<?>> violations) {
        return new ErrorResponse(null,ConstraintViolationError.of(violations),null);
    }

    public static ErrorResponse of(BusinessLogicException businessLogicException) {
        return new ErrorResponse(null,null,BusinessError.of(businessLogicException));
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FieldError {
        private String field;
        private Object rejectedValue;
        private String reason;

        public static List<FieldError> of(BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors =
                    bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ?
                                    "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ConstraintViolationError {
        private String propertyPath;
        private Object rejectedValue;
        private String reason;

        public static List<ConstraintViolationError> of(
                Set<ConstraintViolation<?>> constraintViolations) {
            return constraintViolations.stream()
                    .map(constraintViolation -> new ConstraintViolationError(
                            constraintViolation.getPropertyPath().toString(),
                            constraintViolation.getInvalidValue().toString(),
                            constraintViolation.getMessage()
                    )).collect(Collectors.toList());
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BusinessError {
        private int status;
        private String message;

        public static BusinessError of(BusinessLogicException businessLogicException) {
            return new BusinessError(businessLogicException.getExceptionCode().getStatus(),
                    businessLogicException.getExceptionCode().getMessage());
        }
    }
}
