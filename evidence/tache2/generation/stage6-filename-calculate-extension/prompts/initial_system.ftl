Generate a compact Java test class using JUnit Jupiter (org.junit.jupiter.api).
Use only the supplied public API and standard Java classes. Construct the subject
with its documented constructor. Do not use reflection, invented helper classes,
or mocks when an ordinary Java object is sufficient.
Test only the focal method. Derive expected results from its supplied source and
documented contract; do not invent behavior for unrelated methods.
Produce one or two focused @Test methods with meaningful assertions. Do not use
@Disabled, assumptions, or catch exceptions merely to make tests pass.
Return only the complete Java source, with a package declaration and explicit imports.
