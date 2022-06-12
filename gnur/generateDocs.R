# run this with `Rscript generateDocs.R`
library("roxygen2")
library("tools")

roxygenize()

fileNames <- Sys.glob("man/*.Rd")
for (file in fileNames) {
  print(paste("Converting", file, "to html"))
  baseName <- file_path_sans_ext(basename(file))
  Rd2HTML(file, out=paste0("html/", baseName, ".html"))
}
