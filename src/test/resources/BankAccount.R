#' A Reference Class to represent a bank account.
#'
#' @field balance A length-one numeric vector.
BankAccount <- setRefClass("BankAccount",
                       fields = list(balance = "numeric"),
                       methods = list(
                         withdraw = function(x) {
                           "Withdraw money from account. Allows overdrafts"
                           balance <<- balance - x
                         }
                       )
)