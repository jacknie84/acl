export function isEmail(value: string) {
  const splitPosition = value.lastIndexOf("@");
  if (splitPosition < 0) {
    return false;
  }
  const localPart = value.substring(0, splitPosition);
  const domainPart = value.substring(splitPosition + 1);
  if (!isEmailLocalPart(localPart)) {
    return false;
  }
  if (!isEmailDomainPart(domainPart)) {
    return false;
  }
  return true;
}

const maxLocalPartLength = 64;
const localPartAtom = "[a-z0-9!#$%&'*+/=?^_`{|}~\u0080-\uFFFF-]";
const localPartInsideQuotesAtom = "([a-z0-9!#$%&'*.(),<>\\[\\]:;  @+/=?^_`{|}~\u0080-\uFFFF-]|\\\\\\\\|\\\\\\\")";
const localPartRegExp = new RegExp(
  `(${localPartAtom}|"${localPartInsideQuotesAtom}")(\\.(${localPartAtom}|"${localPartInsideQuotesAtom}"))*`,
  "i",
);

export function isEmailLocalPart(localPart: string) {
  if (localPart.length > maxLocalPartLength) {
    return false;
  }
  if (!localPartRegExp.test(localPart)) {
    return false;
  }
  return true;
}

const maxDomainPartLength = 255;
const domainCharsWithoutDash = "[a-z\u0080-\uFFFF0-9!#$%&'*+/=?^_`{|}~]";
const domainLabel = `(${domainCharsWithoutDash}-*)*${domainCharsWithoutDash}+`;
const domain = `${domainLabel}(\\.${domainLabel})*`;
const ipDomain = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
const ipV6Domain =
  "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";
const emailDomainRegExp = new RegExp(`${domain}|\\[${ipDomain}\\]|\\[IPv6:${ipV6Domain}\\]`, "i");

export function isEmailDomainPart(domainPart: string) {
  if (domainPart.length > maxDomainPartLength) {
    return false;
  }
  if (domainPart.endsWith(".")) {
    return false;
  }
  if (!emailDomainRegExp.test(domainPart)) {
    return false;
  }
  return true;
}
