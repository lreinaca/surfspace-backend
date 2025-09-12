@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "API for managing payments in the system")
public class PaymentController  {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    // CREATE
    @PostMapping
    @Operation(summary = "Create a new payment", description = "Creates a new payment in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PaymentEntity> createPayment(@RequestBody PaymentEntity payment){
        // TODO: implement logic
        return null;
    }

    // READ ALL
    @GetMapping
    @Operation(summary = "Get all payments", description = "Retrieves all payments in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PaymentEntity>> getAllPayments(){
        // TODO: implement logic
        return null;
    }

}

