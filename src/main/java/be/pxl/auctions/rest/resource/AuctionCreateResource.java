package be.pxl.auctions.rest.resource;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AuctionCreateResource {
	@NotBlank
    private String description;
	@NotNull
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate endDate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

	@JsonFormat(pattern = "dd-MM-yyyy")
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
}
