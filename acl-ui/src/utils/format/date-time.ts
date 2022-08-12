import moment from "moment";

export function displayDateTime(formatted?: string) {
  const now = moment();
  const target = moment(formatted);
  const duration = moment.duration(now.diff(target));
  const minutes = Math.floor(duration.asMinutes());
  if (minutes <= 0) {
    return "지금";
  } else if (minutes < 60) {
    return `${minutes}분전`;
  } else if (now.isSame(target, "day")) {
    return target.format("HH:mm:ss");
  } else {
    return target.format("YYYY-MM-DD");
  }
}
