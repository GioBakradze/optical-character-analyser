# optical-character-analyser
Optical character analyser, which operates on scanned images

The very first objectives of this are following
- detecting text regions on scanend images (done)
- detecting symbols on regions (done)
- store symbols as graphs properly to accurately calculate topologycal invariants (almost)

For past few weeks my focus was on thinning symbols, to get proper skeleton, from there i have to store symbols as graphs and calculate topologycal invariants, i think this will be achieved soon.

From there i might implementing some kind of OCR, but i have to consider following things:
- font size
- noise removal, which often breaks characters
- connected characters
